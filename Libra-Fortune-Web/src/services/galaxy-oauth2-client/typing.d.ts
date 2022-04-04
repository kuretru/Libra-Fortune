declare namespace Galaxy.OAuth2.Client {
  type OAuth2AuthorizeRequestDTO = {
    scopes?: string[];
    redirectUri?: string;
  };

  type OAuth2AuthorizeResponseDTO = {
    code: string;
    state: string;
  };
}
