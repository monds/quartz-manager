import {Component} from 'react';
import {Container, FormCheck} from 'react-bootstrap';
import DataGrid from './DataGrid';
import * as schedulerService from './SchedulerAPI';

class JobPage extends Component {

  state = {
    jobData: []
  }

  toJobDetail = (row) => {
    // this.props.history.push(`job/${row['jobName']}`);
  }

  renderCell = (data, i, row) => {
    if (i === 0) return (
      <FormCheck 
        type="switch"
        id="custom-switch"
        label=""
      />
    )
    else return data;
  }

  componentDidMount() {
    schedulerService.getJobs().then(res => {
      console.log(res.data.content)
      this.setState({jobData: res.data.content});
    }); 
  }

  render() {
    const jobColumns = [
      { headerName: '#' },
      { headerName: 'Job Name', field: 'jobName' },
      { headerName: 'class Name', field: 'jobClassName' },
      { headerName: 'Trigger Type', field: 'triggerType' },
      { headerName: 'Trigger State', field: 'triggerState' },
      { headerName: 'Start Time', field: 'startTime' },
      { headerName: 'End Time', field: 'endTime' },
      { headerName: 'Prev Fire Time', field: 'prevFireTime' },
      { headerName: 'Next Fire Time', field: 'nextFireTime' },
      { headerName: 'Cron Expression', field: 'cronExpression' },
    ];
    return (
      <Container style={{ marginTop: '7em' }}>
        {/* <Header as='h1'>Jobs</Header> */}
        <DataGrid 
          columns={jobColumns}
          rowData={this.state.jobData}
          onRowSelected={this.toJobDetail}
          renderCell={this.renderCell}
        />
      </Container>
    )
  }
}

export default JobPage;