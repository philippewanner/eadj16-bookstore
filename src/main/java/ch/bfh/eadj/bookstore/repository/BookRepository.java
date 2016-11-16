package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Jan
 */
public class BookRepository extends AbstractRepository<Book, Long> {

    public BookRepository(EntityManager em) {
        super(em);
    }
    //search(String) List<BookInfo>

    public Book findByISBN(String isbn) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("isbn", isbn.replace(" ", ""));

        List<Book> books = findByNamedQuery("Book.findByISBN", parameters);

        if (books.size() == 0) {
            return null;
        } else {
            return books.get(0);
        }
    }

    public List<Book> findByKeywords(String[] keywords) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root<Book> book = cq.from(Book.class);

        cq.select(book);

        List<Predicate> predicatesOR = new ArrayList<>();

        for (String kw : keywords) {
            List<Predicate> predicates = new ArrayList<>();
            ParameterExpression<String> parameter = cb.parameter(String.class, kw);

            predicates.add(cb.like(cb.lower(book.get("title")), parameter));
            predicates.add(cb.like(cb.lower(book.get("authors")), parameter));
            predicates.add(cb.like(cb.lower(book.get("publisher")), parameter));

            cb.lower(book.get("title"));

            predicatesOR.add(cb.or(predicates.toArray(new Predicate[predicates.size()])));
        }

        cq.where(cb.and(
                predicatesOR.toArray(new Predicate[predicatesOR.size()])));

        TypedQuery<Book> q = em.createQuery(cq);
        for (String kw : keywords) {
            q.setParameter(kw, "%" + kw.toLowerCase() + "%");
        }

        List<Book> books = q.getResultList();

        return books;
    }
}
