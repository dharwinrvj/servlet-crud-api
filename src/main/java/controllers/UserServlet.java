package controllers;

import beans.User;
import daos.UserDAO;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserDAO userDAO;

    public UserServlet() {
        this.userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String jsonRequest = Util.readInputStream(request.getInputStream());
            JSONObject jsonUserObject = new JSONObject(jsonRequest);
            String userName = jsonUserObject.getString("name");
            String userEmail = jsonUserObject.getString("email");
            String userCountry = jsonUserObject.getString("country");
            User user = new User(userName, userEmail, userCountry);
            if (userDAO.insert(user) == 1) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("code", 0);
                jsonResponse.put("message", "The user has been addded");
                jsonResponse.put("user", jsonUserObject);
                response.setStatus(201);
                response.setHeader("Content-Type", "application/json");
                response.getOutputStream().println(jsonResponse.toString());
            }
        } catch (SQLException e) {
            this.printSQLException(e);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userIdURI = request.getPathInfo();
        if (userIdURI == null) {
            this.getAll(response);
        } else {
            int userId = Integer.parseInt(userIdURI.substring(1));
            this.getById(userId, response);
        }

    }

    private void getAll(HttpServletResponse response) throws IOException {
        List<User> users = userDAO.getAll();
        ArrayList<JSONObject> jsonUsersArray = new ArrayList<>();
        for (User user : users) {
            JSONObject jsonUserObject = new JSONObject();
            jsonUserObject.put("id", user.getId());
            jsonUserObject.put("name", user.getName());
            jsonUserObject.put("email", user.getEmail());
            jsonUserObject.put("country", user.getCountry());
            jsonUsersArray.add(jsonUserObject);
        }

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("code", 0);
        jsonResponse.put("message", "success");
        jsonResponse.put("users", jsonUsersArray);
        response.setStatus(200);
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().println(jsonResponse.toString());
    }

    private void getById(int userId, HttpServletResponse response) throws IOException {
        User user = userDAO.getById(userId);
        if (user != null) {
            JSONObject jsonUserObject = new JSONObject();
            jsonUserObject.put("id", user.getId());
            jsonUserObject.put("name", user.getName());
            jsonUserObject.put("email", user.getEmail());
            jsonUserObject.put("country", user.getCountry());
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("code", 0);
            jsonResponse.put("message", "success");
            jsonResponse.put("user", jsonUserObject);
            response.setStatus(200);
            response.setHeader("Content-Type", "application/json");
            response.getOutputStream().println(jsonResponse.toString());
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String userIdURI = request.getPathInfo();
            int userId = Integer.parseInt(userIdURI.substring(1));
            String jsonRequest = Util.readInputStream(request.getInputStream());
            JSONObject jsonUserObject = new JSONObject(jsonRequest);
            String userName = jsonUserObject.getString("name");
            String userEmail = jsonUserObject.getString("email");
            String userCountry = jsonUserObject.getString("country");
            jsonUserObject.put("id", userId);
            User user = new User(userId, userName, userEmail, userCountry);
            Boolean userUpdateFlag = userDAO.update(user);
            if (userUpdateFlag) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("code", 0);
                jsonResponse.put("message", "The user has been updataed");
                jsonResponse.put("user", jsonUserObject);
                response.setStatus(200);
                response.setHeader("Content-Type", "application/json");
                response.getOutputStream().println(jsonResponse.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String userIdURI = request.getPathInfo();
            int userId = Integer.parseInt(userIdURI.substring(1));
            Boolean userDeleteFlag = userDAO.delete(userId);
            if (userDeleteFlag) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("code", 0);
                jsonResponse.put("message", "The user has been deleted");
                response.setStatus(200);
                response.setHeader("Content-Type", "application/json");
                response.getOutputStream().println(jsonResponse.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
