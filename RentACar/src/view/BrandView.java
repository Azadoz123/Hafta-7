package view;

import business.BrandManager;
import core.Helper;
import entity.Brand;

import javax.swing.*;
import java.awt.event.ActionListener;

public class BrandView extends Layout{
    private JPanel container;
    private JLabel lbl_brand;
    private JLabel lbl_brand_name;
    private JTextField fld_brand_name;
    private JButton btn_brand_save;
    private Brand brand;
    private BrandManager brandManager;
    public BrandView(Brand brand) {
        this.brandManager = new BrandManager();
        this.add(container);
        guiInitialize(300, 200);
        this.brand = brand;
        btn_brand_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(this.fld_brand_name)){
                Helper.showMessage("fill");
            }else {
                boolean result = true;
                if(this.brand == null){
                    result = this.brandManager.save(new Brand(fld_brand_name.getText()));
                }
                if(result){
                    Helper.showMessage("done");
                    dispose();
                }else{
                    Helper.showMessage("error");
                }
            }
        });
    }
}
