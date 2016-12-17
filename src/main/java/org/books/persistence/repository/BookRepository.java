package org.books.persistence.repository;

import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jan
 */
@Stateless
public class BookRepository extends AbstractRepository<Book, Long> {

    public BookRepository() {
        super(Book.class);
    }

    public List<Book> findByISBN(String isbn) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("isbn", isbn.replace(" ", ""));

        List<Book> books = findByNamedQuery("Book.findByISBN", parameters);

        return books;
    }

    public List<BookInfo> findInfosByPublicationYear(Integer year) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("year", year);

        List<BookInfo> books = findByNamedQuery(BookInfo.class, "Book.findInfosByPublicationYear", parameters);

        return books;
    }

    public List<BookInfo> getInfosAll() {

        List<BookInfo> books = findByNamedQuery(BookInfo.class, "Book.getInfosAll");

        return books;
    }

    public List<BookInfo> findByKeywords(String[] keywords) {

        if (keywords.length == 1 && (keywords[0].isEmpty() || keywords[0].equals("*"))) {
            return getInfosAll();
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BookInfo> cq = cb.createQuery(BookInfo.class);
        Root<Book> book = cq.from(Book.class);

        cq.multiselect(book.get("isbn"), book.get("title"), book.get("price"));

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

        TypedQuery<BookInfo> q = em.createQuery(cq);
        for (String kw : keywords) {
            q.setParameter(kw, "%" + kw.toLowerCase() + "%");
        }

        List<BookInfo> books = q.getResultList();

        return books;
    }
}
