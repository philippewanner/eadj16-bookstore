/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.eadj.bookstore;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.SalesOrder;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jlue4
 */
public class TestDataProvider {

    private static List<String> isbns = new ArrayList<>();
    
    public static List<Book> getBooks() {
        isbns = new ArrayList<>();
        
        List<Book> books = new ArrayList();
        for (int i = 0; i < 7; i++) {
            Book book = createBook(i);
            books.add(book);
        }

        return books;
    }
    
    public static List<String> getISBNs(){
        return isbns;
    }
    
    private static Book createBook(int i) {
        Book book = new Book();

        switch (i) {
            case 0:
                book.setIsbn("978-3-455-65045-7");
                book.setAuthors("Antoine Laurain");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(192);
                book.setPrice(BigDecimal.valueOf(28.90));
                book.setPublicationYear(2016);
                book.setPublisher("Atlantik Verlag");
                book.setTitle("Das Bild aus meinem Traum");
                
                break;

            case 1:
                book.setIsbn("978-3-352-00885-6");
                book.setAuthors("Kristin Hannah");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(608);
                book.setPrice(BigDecimal.valueOf(25.90));
                book.setPublicationYear(2016);
                book.setPublisher("Ruetten & Loening");
                book.setTitle("Die Nachtigall");
                
                break;

            case 2:
                book.setIsbn("978-3-8105-2471-3");
                book.setAuthors("Lori Nelson Spielman");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(384);
                book.setPrice(BigDecimal.valueOf(21.90));
                book.setPublicationYear(2016);
                book.setPublisher("Fischer Krüger");
                book.setTitle("Und nebenan warten die Sterne");
                break;
                
            case 3:
                book.setIsbn("	978-3-95967-049-4");
                book.setAuthors("Daniel Silva");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(416);
                book.setPrice(BigDecimal.valueOf(22.90));
                book.setPublicationYear(2016);
                book.setPublisher("HarperCollins");
                book.setTitle("Der englische Spion / Gabriel Allon Bd.15");
                break;
                
            case 4:
                book.setIsbn("	978-3-453-41944-5");
                book.setAuthors("Luis Sellano");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(368);
                book.setPrice(BigDecimal.valueOf(22.90));
                book.setPublicationYear(2016);
                book.setPublisher("Heyne");
                book.setTitle("Portugiesisches Erbe");
                break;
                
            case 5:
                book.setIsbn("	978-3-8321-9819-0");
                book.setAuthors("Cay Rademacher");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(304);
                book.setPrice(BigDecimal.valueOf(17.00));
                book.setPublicationYear(2016);
                book.setPublisher("DUMONT Buchverlag");
                book.setTitle("Brennender Midi / Capitaine Roger Blanc Bd. 3");
                break;
                
                case 6:
                book.setIsbn("978-3-455-60055-1");
                book.setAuthors("Agatha Christie");
                book.setBinding(Book.BookBinding.HARDCOVER);
                book.setNumberOfPages(192);
                book.setPrice(BigDecimal.valueOf(22.90));
                book.setPublicationYear(2000);
                book.setPublisher("Atlantik Verlag");
                book.setTitle("Das Geheimnis des Weihnachtspuddings");
                
                break;
        }

        isbns.add(book.getIsbn());
        
        return book;
    }
}
