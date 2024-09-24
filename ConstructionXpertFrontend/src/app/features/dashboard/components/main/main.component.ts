import { Component, ViewEncapsulation } from '@angular/core';
import { ChartData, ChartType, Chart, registerables } from 'chart.js';
import dayGridPlugin from '@fullcalendar/daygrid';
import { CalendarOptions, EventContentArg } from '@fullcalendar/core/index.js';

Chart.register(...registerables);

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  encapsulation: ViewEncapsulation.None,

})
export class MainComponent {
  total_project = 10;
  total_resource = 20;
  total_employee = 5;
  total_budget = 100;

  public doughnutChartData: ChartData<'doughnut'> = {
    labels: ['Projects', 'Resources', 'Employees'],
    datasets: [
      {
        data: [this.total_project, this.total_resource, this.total_employee],
        backgroundColor: ['#FF914C', '#FFBD59', '#D9D9D9']
      }
    ]
  };
  public doughnutChartType: ChartType = 'doughnut';

  public barChartData: ChartData<'bar'> = {
    labels: ['Projects', 'Resources', 'Employees'],
    datasets: [
      {
        label: 'Budget (in K)',
        data: [this.total_budget, 50, 30],
        backgroundColor: ['#FF914C', '#FFBD59', '#D9D9D9']
      }
    ]
  };
  public barChartType: ChartType = 'bar';

  projects = [
    {
      name: 'Project Alpha',
      dateStart: '2024-09-20',
      dateEnd: '2024-09-25',
      progress: 50,
      picture: 'path_to_project_alpha_image',
      userPicture: 'path_to_user_image'
    },
    {
      name: 'Project Beta',
      dateStart: '2024-09-26',
      dateEnd: '2024-10-01',
      progress: 75,
      picture: 'path_to_project_beta_image',
      userPicture: 'path_to_user_image'
    },
  ];

  events: any[] = [];

  calendarOptions!: CalendarOptions;

  ngOnInit() {
    this.initializeEvents();
    this.initializeCalendarOptions();
  }

  initializeEvents() {
    this.events = this.projects.map(project => ({
      title: project.name,
      start: project.dateStart,
      end: project.dateEnd,
      extendedProps: {
        contributors: [project.picture, project.userPicture],
        progress: project.progress
      }
    }));
  }

  initializeCalendarOptions() {
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: [dayGridPlugin],
      events: this.events,
      eventContent: this.eventContent.bind(this),
    };
  }

  eventContent(arg: EventContentArg) {
    const event = arg.event;
    const eventProps = event.extendedProps;

    const card = document.createElement('div');
    card.classList.add('fc-event-card');

    const title = document.createElement('div');
    title.classList.add('fc-title');
    title.textContent = event.title;
    card.appendChild(title);

    if (eventProps['contributors'] && Array.isArray(eventProps['contributors'])) {
      const contributorsContainer = document.createElement('div');
      contributorsContainer.classList.add('fc-contributors');

      eventProps['contributors'].forEach((url: string) => {
        const img = document.createElement('img');
        img.src = url;
        contributorsContainer.appendChild(img);
      });

      card.appendChild(contributorsContainer);
    }

    if (eventProps['progress'] !== undefined) {
      const progressContainer = document.createElement('div');
      progressContainer.classList.add('fc-progress');

      const progressBar = document.createElement('div');
      progressBar.classList.add('fc-progress-bar');
      progressBar.style.width = eventProps['progress'] + '%';

      progressContainer.appendChild(progressBar);
      card.appendChild(progressContainer);
    }

    return { domNodes: [card] };
  }
}
