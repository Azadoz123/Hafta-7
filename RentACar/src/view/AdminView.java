package view;

import business.UserManager;

import javax.swing.*;
import java.awt.*;

public class AdminView extends JFrame{
    private JPanel container;

    public AdminView() {
//        this.userManager = new UserManager();
        this.add(container);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Rent a Car");
        this.setSize(400,400);
    }
}
