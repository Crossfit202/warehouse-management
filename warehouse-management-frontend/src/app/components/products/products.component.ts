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

  errorModalMessage: string | null = null;

  searchTerm: string = '';
  filterField: 'all' | 'sku' | 'name' = 'all';

  sortField: 'sku' | 'name' = 'sku';
  sortDirection: 'asc' | 'desc' = 'asc';

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
        this.closeDeleteModal();
        if (err.status === 409) {
          this.openErrorModal(err.error || 'This product cannot be deleted because it is referenced in inventory or movements.');
        } else {
          this.openErrorModal('Failed to delete product');
        }
      }
    });
  }

  openErrorModal(message: string) {
    this.errorModalMessage = message;
  }

  closeErrorModal() {
    this.errorModalMessage = null;
  }

  get filteredProducts(): any[] {
    if (!this.searchTerm.trim()) return this.products;
    const term = this.searchTerm.trim().toLowerCase();
    return this.products.filter(product =>
      (product.sku?.toLowerCase().includes(term) ||
       product.name?.toLowerCase().includes(term) ||
       product.description?.toLowerCase().includes(term))
    );
  }

  setSort(field: 'sku' | 'name') {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
  }

  get sortedProducts(): any[] {
    const arr = [...this.filteredProducts];
    arr.sort((a, b) => {
      let aVal = (a[this.sortField] || '').toString().toLowerCase();
      let bVal = (b[this.sortField] || '').toString().toLowerCase();
      if (aVal < bVal) return this.sortDirection === 'asc' ? -1 : 1;
      if (aVal > bVal) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });
    return arr;
  }
}
