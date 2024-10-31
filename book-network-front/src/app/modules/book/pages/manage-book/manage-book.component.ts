import {Component, OnInit} from '@angular/core';
import {BookRequest} from "../../../../services/models/book-request";
import {BookService} from "../../../../services/services";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit {
  bookRequest: BookRequest = {authorName: "", isbn: "", shareable: false, synopsis: "", title: ""};
  errorMsg: Array<string>= [];
  selectedPicture: string | undefined;
  selectedBookCover: any;

  constructor(
    private bookService : BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {

  }

  ngOnInit(): void {
    //Recuperer l'id du livre à partir de l'url
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if (bookId) {
      this.bookService.findBookById({
        'bookId': bookId
      }).subscribe({
        next: (book) => {
          this.bookRequest = {
            id: book.id,
            authorName: book.authorName as string,
            isbn: book.isbn as string,
            shareable: book.shareable,
            synopsis: book.synopsis as string,
            title: book.title as string
          };
          console.log(' Book Request : ->><>>>' , this.bookRequest);
          if (book.cover) {
            this.selectedPicture = 'data:image/jpeg;base64,' + book.cover;
          }

        }

      });
    }
  }

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    if(this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.selectedPicture = e.target.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }


  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.saveBookCover({
          'bookId': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-books']);
          }
        });
      },
      error: (err) => {
        console.log(err.error);
        this.errorMsg = err.error.validationErrors;
      }
    });
  }


}
