import { Component, HostBinding, OnInit, Input } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
// Translate
import { TranslationService } from '../../../../../core/_base/layout';

interface LanguageFlag {
  lang: string;
  name: string;
  flag: string;
  active?: boolean;
}

@Component({
  selector: 'kt-language-selector',
  templateUrl: './language-selector.component.html',
  styleUrls:['./language-selector.component.scss']
})
export class LanguageSelectorComponent implements OnInit {
  @Input() iconType: '' | 'brand';

  language: LanguageFlag;
  languages: LanguageFlag[] = [
    // {
    //   lang: 'la',
    //   name: 'Laos',
    //   flag: './assets/media/svg/flags/112-laos.svg'
    // },
    // {
    //   lang: 'en',
    //   name: 'English',
    //   flag: './assets/media/svg/flags/226-united-states.svg'
    // },
    {
      lang: 'vn',
      name: 'Vietnamese',
      flag: './assets/media/svg/flags/220-vietnam.svg'
    }
  ];

  constructor(private translationService: TranslationService, private router: Router) {
  }

  ngOnInit() {
    this.setSelectedLanguage();
    this.router.events
      .pipe(filter(event => event instanceof NavigationStart))
      .subscribe(event => {
        this.setSelectedLanguage();
      });
  }

  setLanguage(lang) {
    this.languages.forEach((language: LanguageFlag) => {
      if (language.lang === lang) {
        language.active = true;
        this.language = language;
      } else {
        language.active = false;
      }
    });
    this.translationService.setLanguage(lang);
    document.querySelector('html').lang = lang
  }

  setSelectedLanguage(): any {
    this.setLanguage(this.translationService.getSelectedLanguage());
  }
}
