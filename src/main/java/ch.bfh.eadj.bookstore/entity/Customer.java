package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer extends BaseEntity {

	@Id
	private Long number;
	private String firstName;
	private String name;
	private String email;
}
