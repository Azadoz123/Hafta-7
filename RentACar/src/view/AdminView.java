package view;

import business.BrandManager;
import entity.Brand;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AdminView extends Layout{
    private JPanel container;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JTabbedPane tab_menu;
    private JButton btn_logout;
    private JPanel pnl_brand;
    private JTable tbl_brand;
    private User user;
    private DefaultTableModel t_mdl_brand = new DefaultTableModel();
    private JPopupMenu brandMenu;

    private BrandManager brandManager;
    public AdminView(User user) {
        this.brandManager = new BrandManager();
        this.add(container);
        guiInitialize(1000,500);
        this.user = user;
        if (this.user == null){
            dispose();
        }

        this.lbl_welcome.setText("Hoşgeldiniz " + this.user.getUsername());

        Object[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Brand> brandArrayList = brandManager.findAll();
        this.t_mdl_brand.setColumnIdentifiers(col_brand);
        for (Brand brand: brandArrayList){
            Object[] obj = {brand.getId(), brand.getName()};
            t_mdl_brand.addRow(obj);
        }

        this.tbl_brand.setModel(t_mdl_brand);
        this.tbl_brand.getTableHeader().setReorderingAllowed(false);
        this.tbl_brand.setEnabled(false);
        this.tbl_brand.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_brand.rowAtPoint(e.getPoint());
                tbl_brand.setRowSelectionInterval(selectedRow,selectedRow);

            }
        });

        this.brandMenu = new JPopupMenu();
        this.brandMenu.add("Yeni").addActionListener(e -> {
            System.out.println("Yeni butonu tıklandı");
        });
        this.brandMenu.add("Güncelle");
        this.brandMenu.add("Sil");

        this.tbl_brand.setComponentPopupMenu(brandMenu);
    }
}
