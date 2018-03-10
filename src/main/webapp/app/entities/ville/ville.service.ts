import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Ville } from './ville.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class VilleService {

    private resourceUrl = SERVER_API_URL + 'api/villes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/villes';

    constructor(private http: Http) { }

    create(ville: Ville): Observable<Ville> {
        const copy = this.convert(ville);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(ville: Ville): Observable<Ville> {
        const copy = this.convert(ville);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Ville> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Ville.
     */
    private convertItemFromServer(json: any): Ville {
        const entity: Ville = Object.assign(new Ville(), json);
        return entity;
    }

    /**
     * Convert a Ville to a JSON which can be sent to the server.
     */
    private convert(ville: Ville): Ville {
        const copy: Ville = Object.assign({}, ville);
        return copy;
    }
}
