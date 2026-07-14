import {
  Component,
  Input,
  ViewChild,
  ElementRef,
  OnChanges,
  SimpleChanges,
  AfterViewInit
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  Chart,
  ChartConfiguration,
  registerables
} from 'chart.js';

import { Burndown } from '../../models/dashboard.model';

Chart.register(...registerables);

@Component({
  selector: 'app-burndown-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './burndown-card.html',
  styleUrl: './burndown-card.css'
})
export class BurndownCard implements OnChanges, AfterViewInit {

  @Input() burndown!: Burndown;

  @ViewChild('chartCanvas')
  chartCanvas!: ElementRef<HTMLCanvasElement>;

  chart!: Chart;

  ngAfterViewInit(): void {

    if (this.burndown) {

      this.createChart();

    }

  }

  ngOnChanges(changes: SimpleChanges): void {

    if (
      changes['burndown'] &&
      this.chart
    ) {

      this.updateChart();

    }

  }

  createChart(): void {

  if (!this.chartCanvas || !this.burndown) {
    return;
  }

  const ctx = this.chartCanvas.nativeElement.getContext('2d');

  if (!ctx) {
    return;
  }

  const gradient = ctx.createLinearGradient(0, 0, 0, 350);

  gradient.addColorStop(0, 'rgba(37,99,235,0.28)');
  gradient.addColorStop(1, 'rgba(37,99,235,0.02)');

  this.chart = new Chart(ctx, {

    type: 'line',

    data: {

      labels: this.burndown.labels,

      datasets: [

        {

          label: 'Actual',

          data: this.burndown.actual,

          borderColor: '#2563EB',

          backgroundColor: gradient,

          borderWidth: 4,

          fill: true,

          tension: 0.35,

          pointRadius: 5,

          pointHoverRadius: 8,

          pointBackgroundColor: '#2563EB',

          pointBorderColor: '#FFFFFF',

          pointBorderWidth: 2

        },

        {

          label: 'Ideal',

          data: this.burndown.ideal,

          borderColor: '#94A3B8',

          borderDash: [8, 6],

          borderWidth: 3,

          fill: false,

          pointRadius: 0,

          tension: 0

        }

      ]

    },

    options: {

      responsive: true,

      maintainAspectRatio:true,

      aspectRatio:2.3,

      interaction: {

        mode: 'index',

        intersect: false

      },

      animation: {

        duration: 1200,

        easing: 'easeOutQuart'

      },

    layout: {

        padding: {

            top: 10,

            bottom: 25,

            left: 10,

            right: 10

        }

    },

      plugins: {

        legend: {

          position: 'top',

          align: 'end',

          labels: {

            usePointStyle: true,

            pointStyle: 'circle',

            padding: 20,

            color: '#334155',

            font: {

              size: 12,

              weight: 'bold'

            }

          }

        },

        tooltip: {

          backgroundColor: '#0F172A',

          titleColor: '#FFFFFF',

          bodyColor: '#E2E8F0',

          padding: 12,

          cornerRadius: 10,

          displayColors: true,

          callbacks: {

            label: (context) =>

              `${context.dataset.label}: ${context.parsed.y} Tasks`

          }

        }

      },

      scales: {

        x: {

          grid: {

            display: false

          },

          ticks: {

            color: '#64748B'

          },

          title: {

            display: true,

            text: 'Sprint Timeline',

            color: '#334155',

            font: {

              size: 13,

              weight: 'bold'

            }

          }

        },

        y: {

          beginAtZero: true,

          grid: {

            color: '#E5E7EB'

          },

          ticks: {

            color: '#64748B',

            stepSize: 1

          },

          title: {

            display: true,

            text: 'Remaining Tasks',

            color: '#334155',

            font: {

              size: 13,

              weight: 'bold'

            }

          }

        }

      }

    }

  });

}
  updateChart(): void {

  if (!this.chart || !this.burndown) {
    return;
  }

  this.chart.data.labels = this.burndown.labels;

  this.chart.data.datasets[0].data = this.burndown.actual;

  this.chart.data.datasets[1].data = this.burndown.ideal;

  this.chart.update('active');

}

}