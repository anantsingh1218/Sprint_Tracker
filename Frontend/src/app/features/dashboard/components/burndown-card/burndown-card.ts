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

    this.createChart();

  }

  ngOnChanges(changes: SimpleChanges): void {

    if (
      changes['burndown'] &&
      this.chart
    ) {

      this.updateChart();

    }

  }

  createChart() {

    if (!this.chartCanvas || !this.burndown) return;

    this.chart = new Chart(this.chartCanvas.nativeElement, {

      type: 'line',

      data: {

        labels: this.burndown.points.map(p => p.label),

        datasets: [

          {

            label: 'Remaining Work',

            data: this.burndown.points.map(p => p.value),

            borderColor: '#2563eb',

            backgroundColor: 'rgba(37,99,235,.15)',

            tension: .35,

            fill: true

          }

        ]

      },

      options: {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

          legend: {

            display: false

          }

        }

      }

    });

  }

  updateChart() {

    this.chart.data.labels =
      this.burndown.points.map(p => p.label);

    this.chart.data.datasets[0].data =
      this.burndown.points.map(p => p.value);

    this.chart.update();

  }

}