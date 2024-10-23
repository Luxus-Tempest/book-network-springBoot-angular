package com.luxus.book_network_api.book;

import com.luxus.book_network_api.common.PageResponse;
import com.luxus.book_network_api.exception.OperationNotPermittedException;
import com.luxus.book_network_api.file.FileStorageService;
import com.luxus.book_network_api.history.BookTransactionHistory;
import com.luxus.book_network_api.history.BookTransactionHistoryRepository;
import com.luxus.book_network_api.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service pour la gestion des livres.
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository transactionHistoryRepository;
    private final FileStorageService fileStorageService;

    /**
     * Enregistre un nouveau livre.
     *
     * @param request       La requête contenant les informations du livre.
     * @param connectedUser L'utilisateur connecté.
     * @return L'identifiant du livre enregistré.
     */
    public Integer save(BookRequest request, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);

        return bookRepository.save(book).getId();
    }

    /**
     * Trouve un livre par son identifiant.
     *
     * @param bookId L'identifiant du livre.
     * @return La réponse contenant les informations du livre.
     */
    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));
    }

    /**
     * Trouve tous les livres disponibles.
     *
     * @param page          Le numéro de la page.
     * @param size          La taille de la page.
     * @param connectedUser L'utilisateur connecté.
     * @return La réponse paginée contenant les livres.
     */
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    /**
     * Trouve tous les livres appartenant à l'utilisateur connecté.
     *
     * @param page          Le numéro de la page.
     * @param size          La taille de la page.
     * @param connectedUser L'utilisateur connecté.
     * @return La réponse paginée contenant les livres de l'utilisateur.
     */
    public PageResponse<BookResponse> findAllBooksByOwner(Integer page, Integer size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    /**
     * Trouve tous les livres empruntés par l'utilisateur connecté.
     *
     * @param page          Le numéro de la page.
     * @param size          La taille de la page.
     * @param connectedUser L'utilisateur connecté.
     * @return La réponse paginée contenant les livres empruntés.
     */
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    /**
     * Trouve tous les livres retournés par l'utilisateur connecté.
     *
     * @param page          Le numéro de la page.
     * @param size          La taille de la page.
     * @param connectedUser L'utilisateur connecté.
     * @return La réponse paginée contenant les livres retournés.
     */
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> booksResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();

        return new PageResponse<>(
                booksResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    /**
     * Met à jour le statut de partage d'un livre.
     *
     * @param bookId        L'identifiant du livre.
     * @param connectedUser L'utilisateur connecté.
     * @return L'identifiant du livre mis à jour.
     */
    public Integer updateSharebleStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));

        User user = ((User) connectedUser.getPrincipal());
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not the owner of this book");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    /**
     * Met à jour le statut d'archivage d'un livre.
     *
     * @param bookId        L'identifiant du livre.
     * @param connectedUser L'utilisateur connecté.
     * @return L'identifiant du livre mis à jour.
     */
    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));

        User user = ((User) connectedUser.getPrincipal());
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not the owner of this book");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    /**
     * Emprunte un livre.
     *
     * @param bookId        L'identifiant du livre.
     * @param connectedUser L'utilisateur connecté.
     * @return L'identifiant du livre emprunté.
     */
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));

        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermittedException("This book cannot be borrow because it is not shareable or it is archived");
        }

        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("You have already borrowed this book");
        }

        BookTransactionHistory history = BookTransactionHistory.builder()
                .book(book)
                .user(user)
                .returnApproved(false)
                .returned(false)
                .build();
        transactionHistoryRepository.save(history);
        return bookId;
    }

    /**
     * Retourne un livre emprunté.
     *
     * @param bookId        L'identifiant du livre.
     * @param connectedUser L'utilisateur connecté.
     * @return L'identifiant de la transaction mise à jour.
     */
    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));

        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermittedException("This book cannot be borrow because it is not shareable or it is archived");
        }

        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

        bookTransactionHistory.setReturned(true);

        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    /**
     * Approuve le retour d'un livre emprunté.
     *
     * @param bookId        L'identifiant du livre.
     * @param connectedUser L'utilisateur connecté.
     * @return L'identifiant de la transaction mise à jour.
     */
    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));

        if (!book.isShareable() || book.isArchived()) {
            throw new OperationNotPermittedException("This book cannot be borrow because it is not shareable or it is archived");
        }

        User user = ((User) connectedUser.getPrincipal());
        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve the return"));

        bookTransactionHistory.setReturnApproved(true);

        return transactionHistoryRepository.save(bookTransactionHistory).getId();
    }


    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with the id: " + bookId));

        User user = ((User) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);

    }
}