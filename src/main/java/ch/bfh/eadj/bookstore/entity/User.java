package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "BookstoreUser")
public class User extends BaseEntity {

	private String name;
	private String password;

        /**
         Type of "Set" because unique required but not ordered
         */
	@ManyToMany
	private Set<Group> groups;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
}