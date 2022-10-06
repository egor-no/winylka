import React from 'react';

const CardList = (Component, props) => {
    const {data} = props;
    if (!data || data.length === 0) return <p>Nothing to show here, sorry</p>;
    return (
        <React.Fragment>
        {
            data.map(item => { return (Component(item))})
        }
        </React.Fragment>
    );
}
export default CardList;
