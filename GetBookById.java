import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import redis.clients.jedis.Jedis;
@WebServlet(urlPatterns = "/GetBookById")
public class GetBookById extends HttpServlet {
	   final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	   final static String DB_URL = "jdbc:mysql://180.76.60.18/linux_Book";
	   final static String USER = "root";
	   final static String PASS = "Aptx4869#";
	   final static String SQL_QURERY_BOOK_BY_ID= "SELECT * FROM book WHERE id=?";
	   static final String REDIS_URL = "180.76.60.18";
	   static Connection conn = null;
	   static Jedis jedis = null;
	   
	   public void init() {
	        try {
	            Class.forName(JDBC_DRIVER);
	            conn = DriverManager.getConnection(DB_URL, USER, PASS);
	            jedis = new Jedis(REDIS_URL);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public void destroy() {
	        try {
	            conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();

	        getServletContext().log(request.getParameter("id"));

	        String json = jedis.get(request.getParameter("id"));

	        if (json == null) {
	            Book stu = getBook(Integer.parseInt(request.getParameter("id")));

	            Gson gson = new Gson();
	            json = gson.toJson(stu, new TypeToken<Book>() {
	            }.getType());

	            jedis.set(request.getParameter("id"), json);
	            out.println(json);

	        } else {
	            out.println(json);
	        }
	        out.flush();
	        out.close();
	    }

	    public Book getBook(int id) {
	        Book stu = new Book();
	        PreparedStatement stmt = null;
	        try {
	            stmt = conn.prepareStatement(SQL_QURERY_BOOK_BY_ID);
	            stmt.setInt(1, id);

	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                stu.id = rs.getInt("id");
	                stu.bname = rs.getString("bname");
	                stu.author = rs.getString("author");
	                stu.publisher= rs.getString("publisher");
	                
	            }

	            rs.close();
	            stmt.close();
	        } catch (SQLException se) {
	            se.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (stmt != null)
	                    stmt.close();
	            } catch (SQLException se) {
	                se.printStackTrace();
	            }
	        }

	        return stu;

	    }
}
class Book {

int id;
String bname;
String author;
String publisher;
public Book() {
	
}
public Book(int id, String bname, String author, String publisher) {
	super();
	this.id = id;
	this.bname = bname;
	this.author = author;
	this.publisher = publisher;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getBname() {
	return bname;
}
public void setBname(String bname) {
	this.bname = bname;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public String getPublisher() {
	return publisher;
}
public void setPublisher(String publisher) {
	this.publisher = publisher;
}
@Override
public String toString() {
	return "Book [id=" + id + ", bname=" + bname + ", author=" + author + ", publisher=" + publisher + "]";
}

}


