package org.books.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BookstoreGroup", uniqueConstraints =
		{ @UniqueConstraint(columnNames = { "name" }) })
public class Group extends BaseEntity {

	@NotNull
	private String name;

	/**
	 * Type of "Set" because unique required but not ordered
	 */
	@ManyToMany(cascade = CascadeType.REMOVE)
	private Set<User> users = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}