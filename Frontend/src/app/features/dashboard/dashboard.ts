import { ChangeDetectorRef, Component, OnInit, ɵinjectChangeDetectorRef } from '@angular/core';
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
import { ExportDashboard } from './models/export-dashboard.model';
import { SprintProgress } from './models/dashboard.model';
import { Router } from '@angular/router';

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

    summary: any;

    sprintProgress?: SprintProgress;

    exportReport?: ExportDashboard;

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

        private dashboardService: DashboardService,
        private cdr: ChangeDetectorRef,
        private router: Router

    ) { }

    ngOnInit(): void {

        this.fetchDashboard();
        this.cdr.detectChanges();

    }

    fetchDashboard(): void {

        this.dashboardService.getDashboard()
            .subscribe({

                next: (res) => {
                    console.log("Dashboard Response =", res);

                    console.log("Role =", res.role);

                    if (res.role === "ROLE_QA" || res.role === "ROLE_BA" || res.role === "ROLE_Developer") {

                        console.log("Redirecting...");

                        this.router.navigate(['/teamDashboard']);

                        return;
                    }

                    this.dashboard = res;

                    console.log("Dashboard Response:", res);

                    this.loading = false;

                    // Only after dashboard is loaded
                    this.fetchProducts();
                    this.cdr.detectChanges();

                },

                error: (err) => {

                    this.loading = false;
                    console.error(err);

                }

            });
        this.cdr.detectChanges();

    }

    fetchSummary(): void {

    if (!this.selectedProductId) {
        return;
    }

    this.dashboardService
        .getSummary(this.selectedProductId)
        .subscribe({

            next: (res: any) => {

                console.log("Summary:", res);

                this.summary = res;
                this.cdr.detectChanges();

            },

            error: err => {

                console.error(err);

            }

        });

}

fetchExport(): void {

    if (!this.selectedProductId) {
        return;
    }

    this.dashboardService
        .exportDashboard(this.selectedProductId)
        .subscribe({

            next: (res: ExportDashboard) => {

                console.log("Export Report:", res);

                this.exportReport = res;
                this.cdr.detectChanges();

            },

            error: err => {

                console.error(err);

            }

        });

}

    fetchProducts(): void {

        this.dashboardService
            .getProducts()
            .subscribe({

                next: (res) => {

                    this.products = res;

                    if (this.products.length > 0) {

                        this.selectedProductId = this.products[0].productId;

                        this.updateSelectedProduct();

                        this.onProductChange();

                    }
                    this.cdr.detectChanges();

                },

                error: (err) => {

                    console.error(err);

                }

            });
        this.cdr.detectChanges();

    }

    onProductChange(): void {

        this.updateSelectedProduct();

        console.log("Fetching Analytics...");

        this.fetchSummary();

        this.fetchSprintProgress();
        
        this.fetchExport();

        this.fetchVelocity();

        this.fetchBurndown();

        this.fetchTeamCapacity();

        this.fetchReleaseReadiness();

        this.cdr.detectChanges();

    }

    fetchVelocity(): void {


        if (!this.selectedProductId) {

            return;

        }

        this.dashboardService.getVelocity(
            this.selectedProductId
        )
            .subscribe({

                next: (res) => {

                    this.velocity = res;

                    console.log(
                        "Velocity",
                        res
                    );
this.cdr.detectChanges();
                },

                error: (err) => {

                    console.error(err);

                }

            });
        this.cdr.detectChanges();

    }

    fetchBurndown(): void {

        this.dashboardService.getBurndown(
            this.selectedProductId
        )
            .subscribe({

                next: (res) => {

                    this.burndown = res;

                    this.cdr.detectChanges();

                },

                error: (err) => {

                    console.error(err);

                }

            });
        this.cdr.detectChanges();

    }

    fetchTeamCapacity(): void {

        if (!this.selectedProductId) {
            return;
        }

        this.dashboardService
            .getTeamCapacity(this.selectedProductId)
            .subscribe({

                next: (res: TeamCapacity) => {

                    this.teamCapacity = res;

                    console.log("Team Capacity", res);
                    this.cdr.detectChanges();

                },

                error: (err: any) => {

                    console.error(err);

                }

            });
        this.cdr.detectChanges();

    }

    fetchReleaseReadiness(): void {

        if (!this.selectedProductId) {
            return;
        }

        this.dashboardService
            .getReleaseReadiness(this.selectedProductId)
            .subscribe({

                next: (res: ReleaseReadiness) => {

                    this.releaseReadiness = res;

                    console.log("Release Readiness", res);
                    this.cdr.detectChanges();

                },

                error: (err: any) => {

                    console.error(err);

                }

            });
        this.cdr.detectChanges();
    }

    fetchSprintProgress(): void {

    if (!this.selectedProductId) {
        return;
    }

    this.dashboardService
        .getSprintProgress(this.selectedProductId)
        .subscribe({

            next: (res) => {

                console.log("Sprint Progress =", res);

                this.sprintProgress = res;
                this.cdr.detectChanges();

            },

            error: (err) => {

                console.error(err);

            }

        });

}
}
