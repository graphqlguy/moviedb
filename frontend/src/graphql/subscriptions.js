import { gql } from '@apollo/client/core';

export const REVIEW_ADDED = gql`
  subscription ReviewAdded($movieId: ID) {
    reviewAdded(movieId: $movieId) {
      review {
        id
        score
        comment
        createdAt
        user { id username }
      }
      movieId
      tvShowId
      title
    }
  }
`;
