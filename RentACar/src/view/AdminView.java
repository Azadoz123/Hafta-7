package view;

import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Brand;
import entity.Car;
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
    private JComboBox<ComboItem> cmb_s_model_brand;
    private JComboBox<Model.Type> cmb_s_model_type;
    private JComboBox<Model.Fuel> cmb_s_model_fuel;
    private JComboBox<Model.Gear> cmb_s_model_gear;
    private JButton btn_search_model;
    private JButton btn_cncl_model;
    private JPanel pnl_car;
    private JTable tbl_car;
    private JPanel pnl_book_searching;
    private JComboBox<Model.Gear> cmb_booking_gear;
    private JComboBox<Model.Type> cmb_booking_type;
    private JComboBox<Model.Fuel> cmb_booking_fuel;
    private JTextField fld_strt_date;
    private JTextField fld_fnsh_date;
    private JLabel lbl_booking_type;
    private JButton btn_booking_search;
    private JTable tbl_booking;
    private User user;
    private DefaultTableModel t_mdl_brand = new DefaultTableModel();
    private DefaultTableModel t_mdl_model = new DefaultTableModel();
    private DefaultTableModel t_mdl_car = new DefaultTableModel();
    private DefaultTableModel t_mdl_booking = new DefaultTableModel();
    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private JPopupMenu car_menu;
    private JPopupMenu booking_menu;
    private BrandManager brandManager;
    private ModelManager modelManager;
    private CarManager carManager;
    private Object[] col_model;
    private Object[] col_car;
    public AdminView(User user) {
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.carManager = new CarManager();

        this.add(container);
        guiInitialize(1000,500);
        this.user = user;
        if (this.user == null){
            dispose();
        }

        this.lbl_welcome.setText("Hoşgeldiniz " + this.user.getUsername());
        loadBrandTable();
        loadBrandComponent();
        
        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();
        
        loadCarTable();
        loadCarComponent();

        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();

    }

    private void loadBookingFilter() {
        this.cmb_booking_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_booking_type.setSelectedItem(null);
        this.cmb_booking_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_booking_gear.setSelectedItem(null);
        this.cmb_booking_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_booking_fuel.setSelectedItem(null);
    }

    private void loadBookingComponent() {
        tableRowSelected(tbl_booking);


        this.booking_menu = new JPopupMenu();
        booking_menu.add("Rezervasyon Yap").addActionListener(e -> {

        });
        btn_booking_search.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.SearchForBooking(
                    fld_strt_date.getText(),
                    fld_fnsh_date.getText(),
                    (Model.Type) cmb_booking_type.getSelectedItem(),
                    (Model.Gear) cmb_booking_gear.getSelectedItem(),
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem()
            );

            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length,carList);
            loadBookingTable(carBookingRow);
        });
    }

    private void loadBookingTable(ArrayList<Object[]> carList) {
        Object[] col_booking = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "Km", "Yıl", "Tip", "Yakıt Türü", "Vites"};
     //   ArrayList<Object[]> carList = this.carManager.getForTable(this.col_car.length, this.carManager.findAll());
        this.createTable(this.t_mdl_booking,this.tbl_booking,col_booking,carList);
    }

    private void loadCarComponent() {
        tableRowSelected(this.tbl_car);
        this.car_menu = new JPopupMenu();
        this.car_menu.add("Yeni").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.car_menu.add("Güncelle").addActionListener(e -> {
            int selectedCarId = this.getTableSelectedRow(tbl_car,0);
             CarView carView = new CarView(this.carManager.getById(selectedCarId));
             carView.addWindowListener(new WindowAdapter() {
                 @Override
                 public void windowClosed(WindowEvent e) {
                     loadCarTable();
                 }
             });
        });
        this.car_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")){
                int selectedCarId = this.getTableSelectedRow(tbl_car,0);
                if (this.carManager.delete(selectedCarId)){
                    Helper.showMessage("done");
                    loadCarTable();
                }else {
                    Helper.showMessage("error");
                }
            }
        });
        tbl_car.setComponentPopupMenu(car_menu);


    }

    private void loadCarTable() {
        this.col_car = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "Km", "Yıl", "Tip", "Yakıt Türü", "Vites"};
           ArrayList<Object[]> carList = this.carManager.getForTable(this.col_car.length, this.carManager.findAll());
        this.createTable(this.t_mdl_car,this.tbl_car,col_car,carList);
    }

    private void loadModelFilter() {
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_s_model_gear.setSelectedItem(null);
        this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_s_model_type.setSelectedItem(null);
        loadModelFilterBrand();
    }
    public void loadModelFilterBrand(){
        this.cmb_s_model_brand.removeAllItems();
        for (Brand brand : this.brandManager.findAll()){
            this.cmb_s_model_brand.addItem(new ComboItem(brand.getId(),brand.getName()));
        }
        this.cmb_s_model_brand.setSelectedItem(null);
    }
    private void loadModelComponent() {
        tableRowSelected(tbl_model);
        this.model_menu = new JPopupMenu();
        this.model_menu.add("Yeni").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
           modelView.addWindowListener(new WindowAdapter() {
               @Override
               public void windowClosed(WindowEvent e) {
                   loadModelTable(null);
               }
           });
        });
        this.model_menu.add("Güncelle").addActionListener(e -> {
            int selectedModelId = this.getTableSelectedRow(tbl_model,0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectedModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        });
        this.model_menu.add("Sil").addActionListener(e ->{
            if(Helper.confirm("sure")){
                int selectedModelId = this.getTableSelectedRow(tbl_model,0);
                if(this.modelManager.delete(selectedModelId)){
                    Helper.showMessage("done");
                    loadModelTable(null);
                }else {
                    Helper.showMessage("error");
                }
            }

        });
        this.tbl_model.setComponentPopupMenu(model_menu);

        this.btn_search_model.addActionListener(e -> {
            ComboItem selectedBrand = (ComboItem) this.cmb_s_model_brand.getSelectedItem();
            int brandId = 0;
            if(selectedBrand != null){
                brandId = selectedBrand.getKey();
            }
            ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                 //   selectedBrand.getKey(),
                    brandId,
                    (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                    (Model.Gear) cmb_s_model_gear.getSelectedItem(),
                    (Model.Type) cmb_s_model_type.getSelectedItem()
            );
            System.out.println(modelListBySearch);

            ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length,modelListBySearch);
            loadModelTable(modelRowListBySearch);

        });

        this.btn_cncl_model.addActionListener(e ->{
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_type.setSelectedItem(null);
            this.cmb_s_model_brand.setSelectedItem(null);
            loadModelTable(null);
        });
    }

    private void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[]{"Model ID", "Marka", "Model Adı", "Tip", "Yıl", "Yakıt Türü", "Vites"};
        if(modelList == null){
            modelList = this.modelManager.getForTable(this.col_model.length, this.modelManager.findAll());
        }
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
                    loadModelTable(null);
                    loadModelFilterBrand();
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
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable();
                }
            });
        });
        this.brand_menu.add("Sil").addActionListener(e ->{
            if(Helper.confirm("sure")){
                int selectedBrandId = this.getTableSelectedRow(tbl_brand,0);
                if(this.brandManager.delete(selectedBrandId)){
                    Helper.showMessage("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable();
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
