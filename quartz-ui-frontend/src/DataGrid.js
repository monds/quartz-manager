import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Pagination, Table } from 'react-bootstrap'

class DataGrid extends Component {
  state = {
    activePage: 1
  }

  renderRow = (columns, row) => {
    return (
      <tr onClick={() => this.props.onRowSelected(row)}>
        {columns.map((column, i) => {
          return (
            <td>{this.props.renderCell(row[column.field] || '', i, row)}</td>
          )
        })}
      </tr>
    )
  }

  renderTable = (columns, rowData) => {
    return (
      <Table bordered hover size="sm">
        <thead>
          <tr>
            {columns.map((column, i) => {
              return (
                <th>{column.headerName}</th>
              )
            })}
          </tr>
        </thead>
        <tbody>
          {rowData.map((row, i) => {
            return this.renderRow(columns, row)
          })}
        </tbody>
      </Table>
    );
  }

  renderCell = (i, data) => {
    return (
      <td>{data}</td>
    )
  }

  changeActivePage = (page) => {
    this.setState({activePage: page});
    this.props.onPageChange(page);
  }

  renderPage = () => {
    let items = [];
    let startPage = Math.floor((this.state.activePage - 1)/10)*10+1;
    let endPage = Math.ceil(this.state.activePage/10)*10;
    
    for (let number = startPage; number <= endPage && number <= this.props.totalPages; number++) {
      items.push(
        <Pagination.Item 
          key={number} 
          active={number === this.state.activePage}
          onClick={() => {this.changeActivePage(number)}}>
          {number}
        </Pagination.Item>
      );
    }
    
    return (
      <Pagination>
        {this.state.activePage > 10 && <Pagination.Prev onClick={() => {
          this.setState({'activePage': startPage - 1});
          this.renderPage();
          this.props.onPageChange(startPage - 1);
        }}/>}
        {items}
        {this.props.totalPages > endPage && <Pagination.Next onClick={() => {
          this.setState({'activePage': endPage + 1});
          this.renderPage();
          this.props.onPageChange(endPage + 1);
        }}/>}
      </Pagination> 
    )
  }

  render() {
    // const maxPage = Math.ceil(this.state.activePage / 10) * 10;
    // const nextItem = this.props.totalPages > maxPage ? '>' : null;
    // console.log(maxPage)
    // console.log(nextItem)
    // const totalPage = this.state.activePage <= 5 ? 5 : this.state.activePage + 2;
    return (
      <div>
        {this.renderTable(this.props.columns, this.props.rowData)}
        {this.renderPage()}
      </div>
    );
  }

}

DataGrid.defaultProps = {
  onRowSelected: () => null,
  renderCell: (data, i, row) => data
}

DataGrid.propTypes = {
  columns: PropTypes.object.isRequired,
  onRowSelected: PropTypes.func,
  renderCell: PropTypes.func,
}

export default DataGrid;