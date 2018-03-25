import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class UI extends JFrame {
    private final int ADD = 0;
    private final int EDIT = 1;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JButton buttonEdit;
    private JLabel countBook;
    private BookModel bookModel;
    private JTable tableCenter;

    public UI() {                                                   // конструктор и основное окно
        super("Libriary");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        ImageIcon iconAdd = new ImageIcon("image/add.jpg");              // кнопка Add
        buttonAdd = new JButton(iconAdd);
        buttonAdd.setToolTipText("Add new book...");
        buttonAdd.setPreferredSize(new Dimension(65, 65));
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrEditBook(ADD);
            }
        });
        northPanel.add(buttonAdd);

        ImageIcon iconRemove = new ImageIcon("image/remove.jpg");        // кнопка Remove
        buttonRemove = new JButton(iconRemove);
        buttonRemove.setToolTipText("Remove book...");
        buttonRemove.setPreferredSize(new Dimension(65, 65));
        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });
        northPanel.add(buttonRemove);

        ImageIcon iconEdit = new ImageIcon("image/edit.jpg");            // кнопка Edit
        buttonEdit = new JButton(iconEdit);
        buttonEdit.setToolTipText("Edit book...");
        buttonEdit.setPreferredSize(new Dimension(65, 65));
        northPanel.add(buttonEdit);
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrEditBook(EDIT);
            }
        });
        add(northPanel, BorderLayout.NORTH);

        bookModel = new BookModel();                                              // создаем таблицу
        tableCenter = new JTable(bookModel);
        // изменяем размеры столбцов
        int prefWidth = 0;
        JTableHeader th = tableCenter.getTableHeader();
        for (int i = 0; i < tableCenter.getColumnCount(); i++) {
            TableColumn column = tableCenter.getColumnModel().getColumn(i);
            int prefWidthMax = 0;
            for (int j = 0; j < tableCenter.getRowCount(); j++) {
                String s = tableCenter.getModel().getValueAt(j, i).toString();
                prefWidth =
                        Math.round(
                                (float) th.getFontMetrics(
                                        th.getFont()).getStringBounds(s,
                                        th.getGraphics()
                                ).getWidth()
                        );
                if ( prefWidth > prefWidthMax ) prefWidthMax = prefWidth;
            }
            column.setPreferredWidth(prefWidthMax + 10);
        }
        JScrollPane scrollPane = new JScrollPane(tableCenter);
        add(scrollPane, BorderLayout.CENTER);

        countBook = new JLabel();                                               // строка состояния (количество книг)
        countBook.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        countBook.setText("Books: " + bookModel.getRowCount());
        add(countBook, BorderLayout.SOUTH);
        setVisible(true);
        //setExtendedState(MAXIMIZED_BOTH);                                       // на весь экран
    }

    /*
                Добавление или редактирование книги в списке
     */
    private void addOrEditBook(int action) {
        int selectedRow = tableCenter.getSelectedRow();       // номер выделенной строки
        if (action == EDIT && tableCenter.getSelectedRow() == -1) return;
        JDialog dialog = new JDialog(this, true);
        dialog.setTitle("Add new book");
        dialog.setSize(400, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7, 2, 10, 5));

        JLabel bookName = new JLabel("Book name:");
        bookName.setHorizontalAlignment(JLabel.RIGHT);
        dialog.add(bookName);
        JTextField bookNameInput = new JTextField();
        dialog.add(bookNameInput);

        JLabel bookPrice = new JLabel("Book price:");
        bookPrice.setHorizontalAlignment(JLabel.RIGHT);
        dialog.add(bookPrice);
        JTextField bookPriceInput = new JTextField();
        dialog.add(bookPriceInput);

        JLabel bookCount = new JLabel("Book count:");
        bookCount.setHorizontalAlignment(JLabel.RIGHT);
        dialog.add(bookCount);
        JTextField bookCountInput = new JTextField();
        dialog.add(bookCountInput);

        JLabel authorName = new JLabel("Author name:");
        authorName.setHorizontalAlignment(JLabel.RIGHT);
        dialog.add(authorName);
        JTextField authorNameInput = new JTextField();
        dialog.add(authorNameInput);

        JLabel authorEmail = new JLabel("Author E-Mail:");
        authorEmail.setHorizontalAlignment(JLabel.RIGHT);
        dialog.add(authorEmail);
        JTextField authorEmailInput = new JTextField();
        dialog.add(authorEmailInput);

        JLabel authorGender = new JLabel("Author gender:");
        authorGender.setHorizontalAlignment(JLabel.RIGHT);
        dialog.add(authorGender);
        JTextField authorGenderInput = new JTextField();
        dialog.add(authorGenderInput);

        JButton okButton = new JButton("OK");
        dialog.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        dialog.add(cancelButton);

        if (action == EDIT) {                                       // если нажата Edit
            int row = tableCenter.getSelectedRow();
            String nameEdit = (String)bookModel.getValueAt(row, 0);
            Author authorEdit = (Author)bookModel.getValueAt(row, 1);
            Double price = (Double)bookModel.getValueAt(row, 2);
            Integer count = (Integer)bookModel.getValueAt(row, 3);
            bookNameInput.setText(nameEdit);
            bookPriceInput.setText(price.toString());
            bookCountInput.setText(count.toString());
            authorNameInput.setText(authorEdit.getName());
            authorEmailInput.setText(authorEdit.getEmail());
            authorGenderInput.setText(authorEdit.getGender());
        }

        okButton.addActionListener(new ActionListener() {           // нажатие кнопки OK
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookNameString = bookNameInput.getText();    // проверка Book name
                if (bookNameString.isEmpty()) {
                    errorDialog("Book name is empty!!!", dialog);
                    return;
                }
                String bookPriceString = bookPriceInput.getText();   // проверка Book price
                if (bookPriceString.isEmpty()) {
                    errorDialog("Book price value is empty!!!", dialog);
                    return;
                }
                double price;
                try {
                    price = Double.parseDouble(bookPriceString);
                } catch (NumberFormatException except) {
                    errorDialog("Book price value incorrect!!!", dialog);
                    return;
                }
                String bookCountString = bookCountInput.getText();  // проверка Book count
                if (bookCountString.isEmpty()) {
                    errorDialog("Book count value is empty!!!", dialog);
                    return;
                }
                int count;
                try {
                    count = Integer.parseInt(bookCountString);
                } catch (NumberFormatException except) {
                    errorDialog("Book count value incorrect!!!", dialog);
                    return;
                }
                String authorName = authorNameInput.getText();      // проверка Author name
                if (authorName.isEmpty()) {
                    errorDialog("Author name is empty!!!", dialog);
                    return;
                }
                String authorEmail = authorEmailInput.getText();    // проверка Author email
                if (authorEmail.isEmpty()) {
                    errorDialog("Author email is empty!!!", dialog);
                    return;
                }
                // валидация email
                if (!authorEmail.matches("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)")) {
                    errorDialog("Invalid email adress!!!", dialog);
                    return;
                }

                String gender = authorGenderInput.getText();        // прверка Author gender
                if (gender.isEmpty() || !(gender.equals("male") || gender.equals("female"))) {
                    errorDialog("Gender must be \"male\" or \"female\"", dialog);
                    return;
                }
                if (action == ADD) {
                    bookModel.addBook(new Book(bookNameString, new Author(authorName, authorEmail, gender), price, count));
                    int row = bookModel.getRowCount() - 1;
                    tableCenter.setRowSelectionInterval(row, row);
                } else {
                    bookModel.editBook(new Book(bookNameString, new Author(authorName, authorEmail, gender), price, count), tableCenter.getSelectedRow());
                    tableCenter.setRowSelectionInterval(selectedRow, selectedRow);
                }
                dialog.dispose();
            }
        }); // конец обработки нажатия кнопки OK

        cancelButton.addActionListener(new ActionListener() {       // нажатие кнопки Cancel
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }); // конец обработки нажатия кнопки Cancel
        dialog.setVisible(true);
        showCount();
    }

    /*
                        Удаление книги из списка
    */
    public void removeBook() {
        int num = tableCenter.getSelectedRow();
        if (num == -1) return;
        JDialog dialog = new JDialog(this, true);
        dialog.setTitle("Remove book");
        dialog.setResizable(false);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(2, 1));
        JLabel label = new JLabel("Do you realy want remove the book?");  // добавляем JLabel
        label.setFont(new Font("", Font.PLAIN, 16));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        dialog.add(label);
        JButton okButton = new JButton("OK");                  // создаем 2 кнопки
        JButton cancelButton = new JButton("Cancel");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        panel.add(okButton);
        panel.add(cancelButton);
        dialog.add(panel);
        okButton.addActionListener(new ActionListener() {           // OK button pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                bookModel.removeBook(num);
                showCount();
                dialog.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {       // Cancel button pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.setVisible(true);
    }                                                                // конец removeBook();

    /*
                        Выводит количество книг в строке состояния
     */
    public void showCount() {
        countBook.setText("Books: " + bookModel.getRowCount());
    }

    /*
                        Вывод диалогового окна с ошибкой
     */
    public void errorDialog(String error, Window component) {
        JDialog dialog = new JDialog(component);
        dialog.setTitle("ERROR!!!");
        dialog.setSize(250, 110);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(component);
        Box box = Box.createVerticalBox();
        JLabel label = new JLabel(error);
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("", Font.PLAIN, 15));
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(label);
        box.add(Box.createVerticalStrut(20));
        JButton okButton = new JButton("OK");
        okButton.setMaximumSize(new Dimension(100, 35));
        okButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        box.add(okButton);
        dialog.setContentPane(box);
        dialog.setVisible(true);
    }                                                                // конец errorDialog(...);
}
