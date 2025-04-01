//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//@WebServlet("/currencies")
//public class Main extends HttpServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write(
//                "[" +
//                        " {" +
//                        "   \"id\": 0," +
//                        "   \"name\": \"United States dollar\"," +
//                        "   \"code\": \"USD\"," +
//                        "   \"sign\": \"$\"," +
//                        " }," +
//                        " {" +
//                        "   \"id\": 0," +
//                        "   \"name\": \"Euro\"," +
//                        "   \"code\": \"EUR\"," +
//                        "   \"sign\": \"ˆ\"," +
//                        " }," +
//                        "]"
//
//        );
//    }
//}