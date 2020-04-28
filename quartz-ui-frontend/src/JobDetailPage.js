import {Component} from 'react';
import {withRouter} from 'react-router-dom';
import {Container} from 'react-bootstrap';
import DataGrid from './DataGrid';
import * as schedulerService from './SchedulerAPI';

class JobDetailPage extends Component {
  state = {
    data: [],
    totalPages: 0
  }

  componentDidMount() {
    schedulerService.getJobHistory(this.props.match.params.job)
    .then(res => {
      this.setState({
        data: res.data.content,
        totalPages: res.data.totalPages - 1
      });
    }); 
  }

  handlePageChange = (page) => {
    schedulerService.getJobHistory(this.props.match.params.job, page)
    .then(res => {
      this.setState({
        data: res.data.content,
        totalPages: res.data.totalPages - 1
      });
    }); 
  }

  render() {
    const columns = [
      { headerName: 'Schedule Name', field: 'schedName' },
      { headerName: 'Entry Id', field: 'entryId' },
      { headerName: 'Job Name', field: 'jobName' },
      { headerName: 'Group Name', field: 'jobGroup' },
      { headerName: 'Start Time', field: 'startTime' },
      { headerName: 'End Time', field: 'endTime' },
    ];
    return (
      <div>
        <Container>
          {this.props.match.params.job}
          <DataGrid 
            columns={columns}
            rowData={this.state.data}
            totalPages={this.state.totalPages}
            onPageChange={this.handlePageChange}
          />
        </Container>
      </div>
    )
  }
}

export default withRouter(JobDetailPage);