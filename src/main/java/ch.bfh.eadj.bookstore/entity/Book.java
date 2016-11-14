package ch.bfh.eadj.bookstore.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by philippewanner on 14.11.16.
 */

@Entity
public class Book {
    
    @Id
    private String isbn;
    
    private String title;
    
    private String authors;
    
    private String publisher;
    
    private Integer publicationYear;
    
    private BookBinding binding;
    
    private Integer numberOfPages;
    
    private BigDecimal price;
    
    public enum BookBinding {
        HARD_COVER, SOFT_COVER
    }
}
