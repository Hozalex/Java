/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ahozyainov
 */
public class Frame extends JFrame {

    int rd;
    int gr;
    int bl;
    JPanel colorPanel;
    JLabel colorText;

    public Frame(String title) {

        //Frame
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(500, 300));
        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.setVisible(true);

        //ColorPanel
        colorPanel = new JPanel();
        colorPanel.setBorder(new LineBorder(Color.white, 60));
        colorPanel.setBackground(new Color(rd, gr, bl));

        //TextLable
        JLabel rdl = new JLabel("Red");
        JLabel grl = new JLabel("Green");
        JLabel bll = new JLabel("Blue");
        JLabel lbl = new JLabel("// ");

        //Text
        colorText = new JLabel();
        colorText.setText("color R:" + rd + " G:" + gr + " B:" + bl);

        //Sliders&Events
        JSlider red = new JSlider(0, 255, 0);
        red.setName("red");
        Event(red);

        JSlider green = new JSlider(0, 255, 0);
        green.setName("green");
        Event(green);

        JSlider blue = new JSlider(0, 255, 0);
        blue.setName("blue");
        Event(blue);

        //Panels
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout());
        southPanel.setBorder(new LineBorder(Color.black, 1));
        southPanel.add(colorText).setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        southPanel.add(lbl).setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JPanel eastPanel = new JPanel();

        //addSliders&TextLabel
        eastPanel.setLayout(new GridLayout(6, 3));
        eastPanel.add(rdl);
        eastPanel.add(red);
        eastPanel.add(grl);
        eastPanel.add(green);
        eastPanel.add(bll);
        eastPanel.add(blue);

        //addToFrame
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.add(eastPanel, BorderLayout.EAST);
        frame.add(colorPanel, BorderLayout.CENTER);

    }

    public void Event(JSlider slider) {

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                switch (slider.getName()) {
                    case "red":
                        rd = slider.getValue();
                        colorPanel.setBackground(new Color(rd, gr, bl));
                        colorText.setText("color R:" + rd + " G:" + gr + " B:" + bl);
                        break;
                    case "blue":
                        bl = slider.getValue();
                        colorPanel.setBackground(new Color(rd, gr, bl));
                        colorText.setText("color R:" + rd + " G:" + gr + " B:" + bl);
                        break;
                    case "green":
                        gr = slider.getValue();
                        colorPanel.setBackground(new Color(rd, gr, bl));
                        colorText.setText("color R:" + rd + " G:" + gr + " B:" + bl);
                        break;
                    default:
                        break;
                }
            }

        });
    }
}
