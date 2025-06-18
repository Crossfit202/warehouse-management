import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventoryItemService } from '../../services/inventory-item.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
  products: any[] = [];
  loading = true;

  showAddModal = false;
  newProduct = { sku: '', name: '', description: '' };

  constructor(private inventoryItemService: InventoryItemService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts() {
    this.inventoryItemService.getAllItems().subscribe({
      next: (items) => {
        this.products = items;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openAddModal() {
    console.log('Add modal opened for:');
    this.newProduct = { sku: '', name: '', description: '' };
    this.showAddModal = true;
  }

  closeAddModal() {
    this.showAddModal = false;
  }

  addProduct() {
    this.inventoryItemService.createItem(this.newProduct).subscribe({
      next: () => {
        this.closeAddModal();
        this.loadProducts();
      },
      error: () => {
        alert('Failed to add product');
      }
    });
  }
}
