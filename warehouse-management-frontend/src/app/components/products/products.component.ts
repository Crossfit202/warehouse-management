import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InventoryItemService } from '../../services/inventory-item.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
  products: any[] = [];
  loading = true;

  constructor(private inventoryItemService: InventoryItemService) { }

  ngOnInit(): void {
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
}
