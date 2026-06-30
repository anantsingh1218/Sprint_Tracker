import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../services/dashboardService';
import { DashboardResponse } from './models/dashboard.model';
import { CommonModule } from '@angular/common';
import { ProductDropdown } from './models/dashboard.model';
import { FormsModule } from '@angular/forms';
import { Velocity } from './models/dashboard.model';
import { Burndown } from './models/dashboard.model';
import { ReleaseReadiness } from './models/dashboard.model';
import { TeamCapacity } from './models/dashboard.model';
import { VelocityCard } from './components/velocity-card/velocity-card';
import { AnalyticsSection } from './components/analytics-section/analytics-section';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, AnalyticsSection],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {

  loading = true;

  dashboard?: DashboardResponse;

  products: ProductDropdown[] = [];

  selectedProductId!: number;

  selectedProduct: any;
  velocity?: Velocity;

burndown?: Burndown;

teamCapacity?: TeamCapacity;

releaseReadiness?: ReleaseReadiness;

private updateSelectedProduct(): void {

    if (
        !this.dashboard ||
        !this.dashboard.dashboard ||
        !this.dashboard.dashboard.products ||
        !this.selectedProductId
    ) {
        return;
    }

    this.selectedProduct =
        this.dashboard.dashboard.products.find(
            (p: any) => p.productId === this.selectedProductId
        );

}

  constructor(
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {

    this.fetchDashboard();

}

  fetchDashboard(): void {

    const userId = 1;

    this.dashboardService.getDashboard(userId)
      .subscribe({

        next: (res) => {

    this.dashboard = res;

    console.log("Dashboard Response:", res);

    this.loading = false;

    // Only after dashboard is loaded
    this.fetchProducts();

},

        error: (err) => {

          this.loading = false;
          console.error(err);

        }

      });

  }

  fetchProducts(): void {

    const userId = 1;

    this.dashboardService
        .getProducts(userId)
        .subscribe({

           next: (res) => {

    this.products = res;

    if (this.products.length > 0) {

        this.selectedProductId = this.products[0].productId;

        this.updateSelectedProduct();

        this.onProductChange();

    }

},

            error: (err) => {

                console.error(err);

            }

        });

}

onProductChange(): void {

  this.updateSelectedProduct();

  console.log("Fetching Analytics...");

  this.fetchVelocity();

  this.fetchBurndown();

  this.fetchTeamCapacity();

  this.fetchReleaseReadiness();

}

fetchVelocity(): void {

    const userId = 1;

    if(!this.selectedProductId){

        return;

    }

    this.dashboardService
        .getVelocity(
            userId,
            this.selectedProductId
        )
        .subscribe({

            next:(res)=>{

                this.velocity = res;

                console.log(
                    "Velocity",
                    res
                );

            },

            error:(err)=>{

                console.error(err);

            }

        });

}

fetchBurndown(): void {

    const userId = 1;

    this.dashboardService
        .getBurndown(
            userId,
            this.selectedProductId
        )
        .subscribe({

            next:(res)=>{

                this.burndown = res;

            },

            error:(err)=>{

                console.error(err);

            }

        });

}

fetchTeamCapacity(): void {

    const userId = 1;

    this.dashboardService
        .getTeamCapacity(
            userId,
            this.selectedProductId
        )
        .subscribe({

            next:(res)=>{

                this.teamCapacity = res;

            },

            error:(err)=>{

                console.error(err);

            }

        });

}

fetchReleaseReadiness(): void {

    const userId = 1;

    this.dashboardService
        .getReleaseReadiness(
            userId,
            this.selectedProductId
        )
        .subscribe({

            next: (res) => {

              console.log("Release Readiness:", res);

              this.releaseReadiness = res;

            },

            error:(err)=>{

                console.error(err);

            }

        });

}
}
