package view;

import business.BookManager;
import core.Helper;
import entity.Book;
import entity.Car;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingView extends Layout{
    private JPanel container;
    private JLabel lbl_car_info;
    private JTextField fld_book_name;
    private JTextField fld_book_idno;
    private JTextField fld_book_mpno;
    private JTextField fld_book_mail;
    private JTextField fkd_book_strt_date;
    private JTextField fld_book_fnsh_date;
    private JTextField fld_book_prc;
    private JTextArea txta_book_note;
    private JButton btn_book_save;
    private Car car;
    private BookManager bookManager;
    public BookingView(Car selectedCar, String strt_date, String fnsh_date) {
        this.car = selectedCar;
        this.bookManager = new BookManager();

        this.add(container);
        guiInitialize(300,600);

        this.lbl_car_info.setText("Araç : " +
                this.car.getPlate()+ " / " +
                this.car.getModel().getBrand().getName() + " / " +
                this.car.getModel().getName());

        this.fkd_book_strt_date.setText(strt_date);
        this.fld_book_fnsh_date.setText(fnsh_date);

        fld_book_name.setText("Mustafa");
        this.fld_book_mpno.setText("12345");
        this.fld_book_mail.setText("test@patika");
        this.fld_book_mpno.setText("555");
        this.fld_book_idno.setText("555");
        this.fld_book_prc.setText("2350");
        this.txta_book_note.setText("Not Alanı");
        btn_book_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField[] checkFieldList= {
                        fld_book_name,
                        fld_book_mail,
                        fld_book_idno,
                        fld_book_mpno,
                        fld_book_prc,
                        fkd_book_strt_date,
                        fld_book_fnsh_date
                };
                if(Helper.isFieldListEmpty(checkFieldList)){
                    Helper.showMessage("Fill");
                }else {
                    Book book = new Book();
                    book.setbCase("done");
                    book.setCar_id(car.getId());
                    book.setName(fld_book_name.getText());
                    book.setStrt_date(LocalDate.parse(strt_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    book.setFnsh_date(LocalDate.parse(fnsh_date, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    book.setIdNo(fld_book_idno.getText());
                    book.setMpNo(fld_book_mpno.getText());
                    book.setMail(fld_book_mail.getText());
                    book.setNote(txta_book_note.getText());
                    book.setPrice(Integer.parseInt(fld_book_prc.getText()));

                    if(bookManager.save(book)){
                        Helper.showMessage("done");
                        dispose();
                    }else {
                        Helper.showMessage("error");
                    }
                }
            }
        });
    }
}
