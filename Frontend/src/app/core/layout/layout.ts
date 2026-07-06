import { Component } from '@angular/core';
import { Sidebar } from '../../shared/sidebar/sidebar';
import { Navbar } from '../../shared/navbar/navbar';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [Sidebar, Navbar, RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {

    collapsed = false;

    toggleSidebar() {

        this.collapsed = !this.collapsed;

    }

}
