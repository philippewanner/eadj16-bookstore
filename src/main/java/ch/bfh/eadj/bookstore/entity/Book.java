package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Book extends BaseEntity {

	public enum BookBinding {
		HARD_COVER, SOFT_COVER
	}

	private String isbn;

	private String title;

	private String authors;

	private String publisher;

	private Integer publicationYear;

	private BookBinding binding;

	private Integer numberOfPages;

	private BigDecimal price;

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}