import { Component, ElementRef, Output, ViewChild, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Output() sidebarToggle = new EventEmitter<boolean>();
  isOpen = true;
  @ViewChild('sidebar', { static: true }) sidebar!: ElementRef;

  toggleSidebar() {
    this.isOpen = !this.isOpen;
    const sidebarElement = this.sidebar.nativeElement;
    sidebarElement.classList.toggle('open', this.isOpen);
    sidebarElement.classList.toggle('closed', !this.isOpen);
    this.sidebarToggle.emit(this.isOpen);
  }
}
