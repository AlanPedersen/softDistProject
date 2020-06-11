package dvd.search.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class DVDSearchApp extends JFrame {
    
    JTextField txtTitle;
    JButton butSearch;
    JList list;
    JScrollPane scrollResults;
    ArrayList <String> dvdList;

    // declare global variables
    static final String DB_URL = "jdbc:mysql://localhost/dvdSearch";
    static final String USER = "root";
    static final String PASS = "";
    
//    final String mainClass = DVDSearchApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//    final String mainDir = mainClass.substring(0, mainClass.indexOf("build"));
    
    public DVDSearchApp() {
        
        setTitle("DVD Search App");
        setLayout(null);
        
        DefaultListModel dlm = new DefaultListModel();
        list = new JList(dlm);
        dvdList = new ArrayList();
        txtTitle = new JTextField();
        txtTitle.setBounds(30, 30, 150, 25);
        butSearch = new JButton("Title Search");
        butSearch.setBounds(200, 30, 120, 25);
        scrollResults = new JScrollPane(list);
        scrollResults.setBounds(30, 85, 290, 150);
        
        try {
// comment out original code
/*          FileInputStream fileInputStream = new FileInputStream(new File(mainDir + "\\TITLES.xls"));
            FileInputStream fileInputStream = new FileInputStream(new File("TITLES.xls"));
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheet("Sheet1");
            
            for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
                HSSFRow row = worksheet.getRow(i);
                HSSFCell title = row.getCell(0);
                dvdList.add(title.getStringCellValue());
            }
*/
// read records from the database
            // create the database connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // execute a query
            Statement stmt = conn.createStatement();
            String sql = "SELECT title FROM tblDvdTitles";
            ResultSet rs = stmt.executeQuery(sql);

            // read the result set adding records to the ArrayList
            while( rs.next())
            {
                dvdList.add(rs.getString("title"));
                
            }
            
            // close connecions
            rs.close();
            stmt.close();

        } catch (Exception e) {
            txtTitle.setText("Failed to Build DVD List");
            System.out.println(e);
        }
        
        butSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    dlm.removeAllElements();
                    
                    for (String curVal : dvdList) {
                        String tempVal = curVal.toLowerCase();
                        if(tempVal.contains(txtTitle.getText().toLowerCase())) {
                            dlm.addElement(curVal);
                        }
                    }
                } catch (Exception e) {
                    txtTitle.setText("Something Went Wrong!");
                    System.out.println(e);
                }
            }
        });
        
        add(txtTitle);
        add(butSearch);
        add(scrollResults);
                
        setSize(360, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DVDSearchApp();
    }
}
