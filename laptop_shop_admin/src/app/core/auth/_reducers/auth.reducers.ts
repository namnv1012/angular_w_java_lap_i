import { AuthActions, AuthActionTypes } from '../_actions/auth.actions';
import { User } from '../_models/user.model';

export interface AuthState {
  loggedIn: boolean;
  authToken: string;
  user: User;
  isUserLoaded: boolean;
}

export const initialAuthState: AuthState = {
  loggedIn: false,
  authToken: undefined,
  user: undefined,
  isUserLoaded: false
};

export function authReducer(state = initialAuthState, action: AuthActions): AuthState {
  switch (action.type) {
    case AuthActionTypes.Login: {
      const token: string = action.payload.authToken;
      return {
        loggedIn: true,
        authToken: token,
        user: undefined,
        isUserLoaded: false
      };
    }

    case AuthActionTypes.Register: {
      const token: string = action.payload.authToken;
      return {
        loggedIn: true,
        authToken: token,
        user: undefined,
        isUserLoaded: false
      };
    }

    case AuthActionTypes.Logout:
      return initialAuthState;

    case AuthActionTypes.UserLoaded: {
      const user: User = action.payload.user;
      return {
        ...state,
        user,
        isUserLoaded: true
      };
    }

    default:
      return state;
  }
}
