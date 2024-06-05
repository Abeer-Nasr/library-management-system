package com.example.librarymanagement.service;

import com.example.librarymanagement.entity.Book;
import com.example.librarymanagement.entity.BorrowingRecord;
import com.example.librarymanagement.entity.Patron;
import com.example.librarymanagement.exception.ResourceNotFoundException;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BorrowingRecordRepository;
import com.example.librarymanagement.repository.PatronRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    private BookRepository bookRepository;
    private PatronRepository patronRepository;

    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecordRepository.findAll();
    }

    public BorrowingRecord addBorrowingRecord(BorrowingRecord borrowingRecord) {
        return borrowingRecordRepository.save(borrowingRecord);
    }
    
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with id: " + patronId));

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(new Date());

        return borrowingRecordRepository.save(borrowingRecord);
    }

    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        // Find the borrowing record for the given book ID and patron ID
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronId(bookId, patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found for book id: " + bookId + " and patron id: " + patronId));

        // Check if the book has already been returned
        if (borrowingRecord.getReturnDate() != null) {
            throw new IllegalStateException("Book with ID " + bookId + " has already been returned by patron with ID " + patronId);
        }

        // Set the return date to the current date and save the changes
        borrowingRecord.setReturnDate(new Date());
        return borrowingRecordRepository.save(borrowingRecord);
    }
    
    public BorrowingRecord updateBorrowingRecord(Long id, BorrowingRecord borrowingRecordDetails) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found"));
        borrowingRecord.setBook(borrowingRecordDetails.getBook());
        borrowingRecord.setPatron(borrowingRecordDetails.getPatron());
        borrowingRecord.setBorrowDate(borrowingRecordDetails.getBorrowDate());
        borrowingRecord.setReturnDate(borrowingRecordDetails.getReturnDate());
        return borrowingRecordRepository.save(borrowingRecord);
    }

    public void deleteBorrowingRecord(Long id) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found"));
        borrowingRecordRepository.delete(borrowingRecord);
    }
}