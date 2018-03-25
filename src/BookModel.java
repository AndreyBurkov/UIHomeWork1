import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;


public class BookModel extends AbstractTableModel {

    private List<Book> books = new ArrayList<>();
    private File file;
    private String filename = "books.dat";

    public BookModel() {
        file = new File(filename);
        try {
            if (!file.exists()) {               // если файла с данными нет, создаем и заполняем
                file.createNewFile();
                Init();
                writeToFile();
            } else {
                readFromFile();
            }
        } catch (IOException e) {
            errorMessage("Error creating file");
        }
    }

    private void Init() {
        books.add(new Book("Java The Complete Reference", new Author("Herbert Shildt","shildt@mail.ru","male"), 100.0, 10));
        books.add(new Book("Core Java", new Author("Cay S. Horstmann","horstmann@google.com","male"), 77.7, 20));
        books.add(new Book("Thinking in Java", new Author("Bruce Eckel","eckel@yandex.ru","male"), 84.6, 9));
        books.add(new Book("Effective Java", new Author("Joshua Bloch","bloch@mail.ru","male"), 91.3, 11));
        books.add(new Book("Data Structures and Algorithms in Java", new Author("Robert Lafore", "lafore@mail.ru", "male"), 85.5, 17));
    }

    // чтение данных из файла
    private void readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            books = (ArrayList<Book>)ois.readObject();
        } catch (Exception e) {
            errorMessage("Read file error");
        }
    }

    // запись данных в файл
    private void writeToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(books);
        } catch (Exception e) {
            errorMessage("Write file error");
        }
    }

    // добавление книги
    public void addBook(Book book) {
        books.add(book);
        writeToFile();
        fireTableDataChanged();
    }

    // удаление книги
    public void removeBook(int num) {
        books.remove(num);
        writeToFile();
        fireTableDataChanged();
    }

    // редактирование книги
    public void editBook(Book book, int row) {
        books.set(row, book);
        writeToFile();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return books.get(rowIndex).getName();
            case 1: return books.get(rowIndex).getAuthor();
            case 2: return books.get(rowIndex).getPrice();
            case 3: return books.get(rowIndex).getCount();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Book name";
            case 1: return "Author";
            case 2: return "Price";
            case 3: return "Count";
        }
        return "";
    }


    private void errorMessage(String message) {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setTitle("ERROR!!!");
        dialog.setSize(100, 100);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(message);
        label.setSize(99, 50);
        dialog.add(label);
        JButton button = new JButton("Exit");
        dialog.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        dialog.setVisible(true);
    }
}
