package by.fpmibsu.edudocs.servlets;

import by.fpmibsu.edudocs.App;
import by.fpmibsu.edudocs.dao.DaoException;
import by.fpmibsu.edudocs.dao.SpecializationDao;
import by.fpmibsu.edudocs.dao.StudentDao;
import by.fpmibsu.edudocs.dao.UserDao;
import by.fpmibsu.edudocs.entities.Student;
import by.fpmibsu.edudocs.entities.Template;
import by.fpmibsu.edudocs.entities.User;
import by.fpmibsu.edudocs.entities.utils.StudentStatus;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;

@WebServlet(name = "AddUser", value = "/add-student")
public class AddStudent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StudentDao userDao = new StudentDao();
        SpecializationDao sd = new SpecializationDao();

        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        String url = "jdbc:sqlserver://localhost";
        Properties prop = new Properties();
        InputStream stream = App.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            Connection con = DriverManager.getConnection(url, prop);
            userDao.setConnection(con);
            sd.setConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (request.getParameter("login") == null ||
                request.getParameter("password") == null ||
                request.getParameter("name") == null ||
                request.getParameter("lastname") == null ||
                request.getParameter("entryDate") == null ||
                request.getParameter("group") == null ||
                request.getParameter("status") == null ||
                request.getParameter("uniqueNumber") == null ||
                request.getParameter("specialization") == null){
            response.setStatus(422);
            return;
        }
       try {
            User user = new Student(null,
                    request.getParameter("login"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("surname"),
                    request.getParameter("lastname"),
                    Timestamp.valueOf(request.getParameter("entryDate")),
                    Integer.valueOf(request.getParameter("group")),
                    Integer.valueOf(request.getParameter("uniqueNumber")),
                    StudentStatus.valueOf(request.getParameter("status")),
                    sd.findEntityById(UUID.fromString((request.getParameter("specialization")))));
            if(userDao.create((Student) user)){

            }
        } catch (DaoException e) {
            response.setStatus(422);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}