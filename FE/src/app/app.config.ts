import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  // provideHttpClient(): bật HttpClient toàn app để gọi API BE (trang /admin cần).
  providers: [provideRouter(routes), provideHttpClient()],
};
