package view;

import business.BrandManager;
import core.Helper;
import entity.Brand;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
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
        loadBrandTable();
        loadBrandComponent();

        this.tbl_brand.setComponentPopupMenu(brandMenu);
    }

    private void loadBrandComponent() {
        this.tbl_brand.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_brand.rowAtPoint(e.getPoint());
                tbl_brand.setRowSelectionInterval(selectedRow,selectedRow);

            }
        });

        this.brandMenu = new JPopupMenu();
        this.brandMenu.add("Yeni").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                }
            });
        });
        this.brandMenu.add("Güncelle").addActionListener(e -> {
            int selectedBrandId = this.getTableSelectedRow(tbl_brand,0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectedBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                }
            });
        });
        this.brandMenu.add("Sil").addActionListener(e ->{
            if(Helper.confirm("sure")){
                int selectedBrandId = this.getTableSelectedRow(tbl_brand,0);
                if(this.brandManager.delete(selectedBrandId)){
                    Helper.showMessage("done");
                    loadBrandTable();
                }else {
                    Helper.showMessage("error");
                }
            }

        });
    }

    public void loadBrandTable(){
        Object[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.t_mdl_brand,this.tbl_brand,col_brand,brandList);
    }

}
