package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Book extends BaseEntity {

	public enum BookBinding {
		HARD_COVER, SOFT_COVER
	}

	@Id
	private String isbn;

	private String title;

	private String authors;

	private String publisher;

	private Integer publicationYear;

	private BookBinding binding;

	private Integer numberOfPages;

	private BigDecimal price;

}