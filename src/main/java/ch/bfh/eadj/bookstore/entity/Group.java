package ch.bfh.eadj.bookstore.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BookstoreGroup")
public class Group extends BaseEntity {

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