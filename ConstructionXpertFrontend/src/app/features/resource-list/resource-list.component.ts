import { Component, OnInit } from '@angular/core';
import {ResourceDto} from "../../core/dtos/ResourceDto";
import {ResourceService} from "../../core/services/resource.service";


@Component({
  selector: 'app-resource-list',
  templateUrl: './resource-list.component.html',
  styleUrls: ['./resource-list.component.css']
})
export class ResourceListComponent implements OnInit {
  resources: ResourceDto[] = [];
  selectedResource: ResourceDto | null = null;
  isEditing: boolean = false;

  constructor(private resourceService: ResourceService) {}

  ngOnInit(): void {
    this.loadResources();
  }

  loadResources(): void {
    this.resourceService.getAllResources().subscribe(
      (data: ResourceDto[]) => {
        this.resources = data;
      },
      (error) => {
        console.error('Error loading resources', error);
      }
    );
  }

  selectResource(resource: ResourceDto): void {
    this.selectedResource = { ...resource };
    this.isEditing = true;
  }

  saveResource(): void {
    if (this.selectedResource) {
      if (this.selectedResource.id) {
        this.resourceService.updateResource(this.selectedResource.id, this.selectedResource).subscribe(
          (data: ResourceDto) => {
            this.loadResources();
            this.cancelEdit();
          },
          (error) => {
            console.error('Error updating resource', error);
          }
        );
      } else {
        this.resourceService.createResource(this.selectedResource).subscribe(
          (data: ResourceDto) => {
            this.loadResources();
            this.cancelEdit();
          },
          (error) => {
            console.error('Error creating resource', error);
          }
        );
      }
    }
  }

  deleteResource(id: number): void {
    this.resourceService.deleteResource(id).subscribe(
      () => {
        this.loadResources();
      },
      (error) => {
        console.error('Error deleting resource', error);
      }
    );
  }

  cancelEdit(): void {
    this.selectedResource = null;
    this.isEditing = false;
  }
}
