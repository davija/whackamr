import { MergeRequestDto } from '@models/app.model';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';

export enum MergeRequestView {
  All,
  InReview,
  Mine,
}

export interface MergeRequestState {
  mergeRequestData: MergeRequestDto[];
  activeView: MergeRequestView;
  loading: boolean;
}

const initialState: MergeRequestState = {
  activeView: MergeRequestView.InReview,
  mergeRequestData: [],
  loading: false,
};

export const MergeRequestStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed(() => ({})),
  withMethods(store => ({
    setActiveView(activeView: MergeRequestView): void {
      patchState(store, state => ({ ...state, activeView }));
    },
    setLoading(loading: boolean): void {
      patchState(store, state => ({ ...state, loading }));
    },
    setMergeRequests(mergeRequestData: MergeRequestDto[]): void {
      patchState(store, state => ({ ...state, mergeRequestData }));
    },
  })),
);
