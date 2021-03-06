import React, { Component } from 'react';
import { Container, FormCheck } from 'react-bootstrap';
import DataGrid from './DataGrid';
import * as schedulerService from './SchedulerAPI';

class JobPage extends Component {

  state = {
    jobData: [],
    jobState: {}
  }

  toJobDetail = (e) => {
    // this.props.history.push(`job/${row['jobName']}`);
  }

  handleJobState = (e) => {
    if (!window.confirm('정말입니까?')) {
      e.preventDefault();
      return;
    }
    let jobState = this.state.jobState;
    Object.keys(jobState).forEach(jobName => {
      if (jobName === e.target.id)
        jobState[jobName] = e.target.checked;
    })
    this.setState({ jobState });
  }

  renderCell = (data, i, row) => {
    if (i === 0) {
      return (
        <FormCheck 
          type="switch"
          id={row['jobName']}
          label=""
          defaultChecked={this.state.jobState[row['jobName']] || false}
          onClick={this.handleJobState}
        />
      )
    } else if (i === 1) {
      return (
        <a href="#" onClick={e => {
          e.preventDefault();
          this.props.history.push(`job/${row['jobName']}`);
        }}>{row['jobName']}</a>
      )
    } else {
      return data;
    }
  }

  componentDidMount() {
    schedulerService.getJobs().then(res => {
      const jobState = {};
      res.data.content.forEach(job => {
        jobState[job['jobName']] = job['triggerState'] !== 'PAUSED';
      })
      this.setState({ jobState });
      this.setState({jobData: res.data.content});
    });
  }

  render() {
    const jobColumns = [
      { headerName: '#' },
      { headerName: 'Job Name', field: 'jobName' },
      { headerName: 'class Name', field: 'jobClassName' },
      { headerName: 'Trigger Type', field: 'triggerType' },
      { headerName: 'Start Time', field: 'startTime' },
      { headerName: 'End Time', field: 'endTime' },
      { headerName: 'Prev Fire Time', field: 'prevFireTime' },
      { headerName: 'Next Fire Time', field: 'nextFireTime' },
      { headerName: 'Cron Expression', field: 'cronExpression' },
    ];
    return (
      <Container style={{ marginTop: '7em' }}>
        <h2>Jobs</h2>
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