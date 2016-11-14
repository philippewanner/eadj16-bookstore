package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Group extends BaseEntity {

	private String name;

        /**
         Type of "Set" because unique required but not ordered
         */
	@ManyToMany
	private Set<User> users;

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