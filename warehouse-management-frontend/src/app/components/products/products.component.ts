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

  showEditModal = false;
  editProduct: any = null;

  showDeleteModal = false;
  deleteProduct: any = null;

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

  openEditModal(product: any) {
    this.editProduct = { ...product };
    this.showEditModal = true;
  }
  closeEditModal() {
    this.showEditModal = false;
  }

  updateProduct() {
    this.inventoryItemService.updateItem(this.editProduct).subscribe({
      next: () => {
        this.closeEditModal();
        this.loadProducts();
      },
      error: () => {
        alert('Failed to update product');
      }
    });
  }

  openDeleteModal(product: any) {
    this.deleteProduct = product;
    this.showDeleteModal = true;
  }
  closeDeleteModal() {
    this.showDeleteModal = false;
  }
  deleteProductConfirmed() {
    this.inventoryItemService.deleteItem(this.deleteProduct.id).subscribe({
      next: () => {
        this.closeDeleteModal();
        this.loadProducts();
      },
      error: (err) => {
        if (err.status === 409) {
          alert(err.error || 'This product cannot be deleted because it is referenced in inventory or movements.');
        } else {
          alert('Failed to delete product');
        }
      }
    });
  }
}
