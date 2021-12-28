import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/UpdateBook")
public class UpdateBook extends HttpServlet{
	 final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	   final static String URL = "jdbc:mysql://180.76.60.18/linux_Book";
	   final static String USER = "root";
	   final static String PASS = "Aptx4869#";
    final static String UPDATE_BOOK_BY_ID = "UPDATE book set bname=?,author=?,publisher=? WHERE id=?";
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Book req = getRequestBody(request);
        PrintWriter out = response.getWriter();

        out.println(updateBook(req));
        out.flush();
        out.close();
     }

    private Book getRequestBody(HttpServletRequest request) throws IOException {
        Book stu = new Book();
        StringBuffer bodyJ = new StringBuffer();
        String line = null;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
          bodyJ.append(line);
        Gson gson = new Gson();
        stu = gson.fromJson(bodyJ.toString(), new TypeToken<Book>() {
        }.getType());
        return stu;
     }

     private int updateBook(Book req) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int retcode = -1;
        try {
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(URL, USER, PASS);
           stmt = conn.prepareStatement(UPDATE_BOOK_BY_ID);
          
           stmt.setString(1, req.bname);
           stmt.setString(2, req.author);
           stmt.setString(3,req.publisher);

           int row = stmt.executeUpdate();
           if (row > 0)
              retcode = 1;

           stmt.close();
           conn.close();
        } catch (SQLException se) {
           se.printStackTrace();
        } catch (Exception e) {
           e.printStackTrace();
  } finally {
           try {
              if (stmt != null)
                 stmt.close();
              if (conn != null)
                 conn.close();
          } catch (SQLException se) {
              se.printStackTrace();
           }
        }

           return retcode;
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

