import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Employe } from './employe.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EmployeService {

    private resourceUrl = SERVER_API_URL + 'api/employes';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/employes';

    constructor(private http: Http) { }

    create(employe: Employe): Observable<Employe> {
        const copy = this.convert(employe);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(employe: Employe): Observable<Employe> {
        const copy = this.convert(employe);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Employe> {
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
     * Convert a returned JSON object to Employe.
     */
    private convertItemFromServer(json: any): Employe {
        const entity: Employe = Object.assign(new Employe(), json);
        return entity;
    }

    /**
     * Convert a Employe to a JSON which can be sent to the server.
     */
    private convert(employe: Employe): Employe {
        const copy: Employe = Object.assign({}, employe);
        return copy;
    }
}
