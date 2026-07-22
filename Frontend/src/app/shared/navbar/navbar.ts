import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth';
import { UpperCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SearchService } from '../../features/search/search.service';

interface SearchItem {
  id: number;
  type: string;
  name: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [UpperCasePipe, FormsModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent implements OnInit {

  username: string | null = null;
  role: string | null = null;
  isMenuOpen: boolean = false;

  searchText: string = '';
  searchResults: SearchItem[] = [];

  constructor(
    private authService: AuthService,
    private router: Router,
    private searchService: SearchService
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.role = this.authService.getRole();
  }

  onSearch(): void {
    if (!this.searchText.trim()) {
      this.searchResults = [];
      return;
    }

    this.searchService.search(this.searchText).subscribe({
      next: (res: SearchItem[]) => {
        this.searchResults = res;
      },
      error: (err: any) => {
        console.error('Search Error:', err);
      }
    });
  }

  get isProjectManager(): boolean {
    return this.role?.endsWith('PM') ?? false;
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  goToProfile(): void {
    if (this.router.url === '/profile') {
      window.location.reload();
    } else {
      this.router.navigate(['/profile']);
    }

    this.isMenuOpen = false;
  }

  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('role');
    localStorage.removeItem('username');

    this.router.navigate(['/login']);
  }

  navigateToItem(item: SearchItem): void {

      console.log("Clicked item:", item);
    switch (item.type.toUpperCase()) {
     case 'TASK':
        this.router.navigateByUrl(`/tasks/${item.id}`);
        break;

      case 'BUG':
        this.router.navigate(['/bugs', item.id]);
        break;

      case 'STORY':
        this.router.navigate(['/story', item.id]);
        break;

      case 'FEATURE':
        this.router.navigate(['/feature', item.id]);
        break;

      case 'PRODUCT':
        this.router.navigate(['/product', item.id]);
        break;

      case 'SPRINT':
       this.router.navigateByUrl(`/sprints/${item.id}`)
        .then(success => console.log('Navigation success:', success));
        break;


  case 'DSU':
  this.router.navigateByUrl(`/dsu/${item.id}`);
  break;
    }

    this.searchResults = [];
    this.searchText = '';
  }
}
