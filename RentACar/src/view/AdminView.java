package view;

import business.BrandManager;
import business.ModelManager;
import core.Helper;
import entity.Model;
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
    private JPanel pnl_model;
    private JTable tbl_model;
    private JComboBox cmb_s_model_brand;
    private JComboBox cmb_s_model_type;
    private JComboBox cmb_s_model_fuel;
    private JComboBox cmb_s_model_gear;
    private JButton aramaYapButton;
    private User user;
    private DefaultTableModel t_mdl_brand = new DefaultTableModel();
    private DefaultTableModel t_mdl_model = new DefaultTableModel();
    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private BrandManager brandManager;
    private ModelManager modelManager;
    public AdminView(User user) {
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();

        this.add(container);
        guiInitialize(1000,500);
        this.user = user;
        if (this.user == null){
            dispose();
        }

        this.lbl_welcome.setText("Hoşgeldiniz " + this.user.getUsername());
        loadBrandTable();
        loadBrandComponent();
        
        loadModelTable();
        loadModelComponent();
    }

    private void loadModelComponent() {
        tableRowSelected(tbl_model);
        this.model_menu = new JPopupMenu();
        this.model_menu.add("Yeni").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
           modelView.addWindowListener(new WindowAdapter() {
               @Override
               public void windowClosed(WindowEvent e) {
                   loadModelTable();
               }
           });
        });
        this.model_menu.add("Güncelle").addActionListener(e -> {
            int selectedModelId = this.getTableSelectedRow(tbl_model,0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectedModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable();
                }
            });
        });
        this.model_menu.add("Sil").addActionListener(e ->{
            if(Helper.confirm("sure")){
                int selectedModelId = this.getTableSelectedRow(tbl_model,0);
                if(this.modelManager.delete(selectedModelId)){
                    Helper.showMessage("done");
                    loadModelTable();
                }else {
                    Helper.showMessage("error");
                }
            }

        });
        this.tbl_model.setComponentPopupMenu(model_menu);
    }

    private void loadModelTable() {
        Object[] col_model = {"Model ID", "Marka", "Model Adı", "Tip", "Yıl", "Yakıt Türü", "Vites"};
        ArrayList<Object[]> modelList = this.modelManager.getForTable(col_model.length, this.modelManager.findAll());
        this.createTable(this.t_mdl_model,this.tbl_model,col_model,modelList);
    }

    private void loadBrandComponent() {
        this.tbl_brand.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tbl_brand.rowAtPoint(e.getPoint());
                tbl_brand.setRowSelectionInterval(selectedRow,selectedRow);

            }
        });

        this.brand_menu = new JPopupMenu();
        this.brand_menu.add("Yeni").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable();
                }
            });
        });
        this.brand_menu.add("Güncelle").addActionListener(e -> {
            int selectedBrandId = this.getTableSelectedRow(tbl_brand,0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectedBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable();
                }
            });
        });
        this.brand_menu.add("Sil").addActionListener(e ->{
            if(Helper.confirm("sure")){
                int selectedBrandId = this.getTableSelectedRow(tbl_brand,0);
                if(this.brandManager.delete(selectedBrandId)){
                    Helper.showMessage("done");
                    loadBrandTable();
                    loadModelTable();
                }else {
                    Helper.showMessage("error");
                }
            }

        });
        this.tbl_brand.setComponentPopupMenu(brand_menu);
    }

    public void loadBrandTable(){
        Object[] col_brand = {"Marka ID", "Marka Adı"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.t_mdl_brand,this.tbl_brand,col_brand,brandList);
    }

}
