package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Group extends BaseEntity {

	@Id
	private String name;
}